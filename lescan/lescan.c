#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <errno.h>
#include <ctype.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <getopt.h>
#include <sys/param.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <sys/file.h>
#include <signal.h>
#include <time.h>

#include "bluetooth.h"
#include "hci.h"
#include "hci_lib.h"

static int maxTime = -1;
static char debug = 0;

static volatile int signal_received = 0;

static int nDevs;
static bdaddr_t *devsBtAddr;

static void sigint_handler(int sig)
{
	signal_received = sig;
}

static int getVal16(uint8_t* data) {
	int t = (data[1]&0xff)<<8;
	t += (data[0]&0xff);
	return t;
}



static int print_advertising_devices(int dd, uint8_t filter_type)
{
	unsigned char buf[HCI_MAX_EVENT_SIZE], *ptr;
	struct hci_filter nf, of;
	struct sigaction sa;
	socklen_t olen;
	int len = 0;
	int origlen;
	int i;

	olen = sizeof(of);
	if (getsockopt(dd, SOL_HCI, HCI_FILTER, &of, &olen) < 0) {
		printf("Could not get socket options\n");
		return -1;
	}

	hci_filter_clear(&nf);
	hci_filter_set_ptype(HCI_EVENT_PKT, &nf);
	hci_filter_set_event(EVT_LE_META_EVENT, &nf);

	if (setsockopt(dd, SOL_HCI, HCI_FILTER, &nf, sizeof(nf)) < 0) {
		printf("Could not set socket options\n");
		return -1;
	}

	memset(&sa, 0, sizeof(sa));
	sa.sa_flags = SA_NOCLDSTOP;
	sa.sa_handler = sigint_handler;
	sigaction(SIGINT, &sa, NULL);
	time_t startTime = time(NULL);

	/*

	typedef struct {
		uint8_t b[6];
	} __attribute__((packed)) bdaddr_t;

	typedef struct {
		uint8_t         subevent;
		uint8_t         data[0];
	} __attribute__ ((packed)) evt_le_meta_event;

	typedef struct {
		uint8_t         evt_type;
		uint8_t         bdaddr_type;
		bdaddr_t        bdaddr;
		uint8_t         length;
		uint8_t         data[0];
	} __attribute__ ((packed)) le_advertising_info;
	*/
	evt_le_meta_event *meta;
	le_advertising_info *info;
	hci_event_hdr *hdr;
	fd_set set;
	struct timeval timeout;
	int rv = 1;
	int nreports;
	char addr[18];
	while (rv > 0) {
		len = 0;
		FD_ZERO(&set); /* clear the set */
		FD_SET(dd, &set); /* add our file descriptor to the set */
		if (maxTime > 0) {
			timeout.tv_sec = maxTime - (time(NULL) - startTime);
			timeout.tv_usec = 0;
			if (timeout.tv_sec <= 0)
				goto done;
			rv = select(dd + 1, &set, NULL, NULL, &timeout);
		} else
			rv = select(dd + 1, &set, NULL, NULL, NULL);

		if(rv == -1) { /* an error accured */
			perror("select");
			len = -1;
			goto done;
		} else if (rv == 0) { /* a timeout occured */
			goto done;
		}
		len = origlen = read(dd, buf, sizeof(buf));

		if (debug) {
			for (i=0; i < origlen; i++) {
				printf("%02X ", (unsigned int)(buf[i]& 0xFF));
			}
			printf("\n");
		}


		if (buf[0] & 0xff != HCI_EVENT_PKT) {
			printf("Unexpected packet type %02x\n", buf[0]);
			goto done;
		}

		hdr = (void *) buf + 1;
		if (hdr->evt != EVT_LE_META_EVENT) {
			printf("Unexpected event type %02x\n", hdr->evt);
			goto done;
		}
		if (len != hdr->plen + HCI_EVENT_HDR_SIZE + 1) {
			printf("Wrong length: buffer len is %02x  <> payload len %02x + hdr size %02x + 1\n", len, hdr->plen, HCI_EVENT_HDR_SIZE);
			goto done;
		}

/*
		ptr = buf + (1 + HCI_EVENT_HDR_SIZE);
		len -= (1 + HCI_EVENT_HDR_SIZE);

		meta = (void *) ptr;
*/
		meta = (void *) (((unsigned char *) hdr) + HCI_EVENT_HDR_SIZE);

		if (meta->subevent != EVT_LE_ADVERTISING_REPORT) {
			printf("Ignoring event type %02x <> EVT_LE_ADVERTISING_REPORT %02x\n", meta->subevent, EVT_LE_ADVERTISING_REPORT);
			goto done;
		}

		nreports = (int) meta->data[0];
		printf("nreports is %02x\n", nreports);


		ptr = meta->data + 1;

		for (int i = 0; i < nreports; i++) {
			info = (le_advertising_info *) ptr;
			if (debug) {
				ba2str(&info->bdaddr, addr);
				printf(
					"Report %d: et: %02x, at; %02x, len: %02x, addr: %s\n", 
					i, info->evt_type, info->bdaddr_type, info->length, addr);
			}

			for (i=0; i<nDevs; i++) {
				if (memcmp(info->bdaddr.b, devsBtAddr[i].b, sizeof(bdaddr_t)) == 0) {
					update_data(i, info->data, info->length);
					break; 
				}
			}

			ptr = ptr + info->length;

		}



		/* check if enough samples or timeout */
		if ((maxTime > 0) && (time(NULL) - startTime) > maxTime)
			goto done;
		int exit = 1;
		for (i=0; i<nDevs; i++) {
			if (ntSamples[i] < numSamples || nhSamples[i] < numSamples)
				exit = 0;
		}
		if (exit == 1)
			goto done;
	}

done:
	setsockopt(dd, SOL_HCI, HCI_FILTER, &of, sizeof(of));

	if (len < 0)
		return -1;

	return 0;
}


static void usage(void)
{
	int i;

	printf("scanMijia - ver 1.0\n");
	printf("Usage:\n"
		"\tscanMijia [options] BT adress list\n");
	printf("Options:\n"
		"\t-h\t\tDisplay help\n"
		"\t-i dev\t\tHCI device\n"
		"\t-d\t\tPrint raw data to stdout (debug)\n"
		"\t-f filename\tPrint output values to the file (default stdout)\n"
		"\t-t timeout\tStop after timout seconds (default no timeout)\n"
		"\t-n num\t\tOutput the average and stops after num samples\n"
		"\t\t\t(default 1)\n");
	printf("Output:\n"
		"\tOne line for each value. The format is:\n"
		"\t<TAG> <BT Address> <Timestamp> <Value>\n"
		"\t<TAG>: one of 'T'(temperature) 'H'(humidity) 'B'(battery)\n"
		"\t<Timestamp>: Unix timestamp (seconds since January 1, 1970)\n"
		"\t<Value>: float for 'T' and 'H', integer for 'B' \n");
		
		
}

void outputValues();

int main(int argc, char *argv[])
{
	int err, opt, dd;
	uint8_t own_type = LE_PUBLIC_ADDRESS; /* (other option LE_RANDOM_ADDRESS) */
	uint8_t scan_type = 0x00; /* Passive (0x01 = normal scan) */ 
	uint8_t filter_type = 0;
	uint8_t filter_policy = 0x01; /* Whitelist (0x00 = normal scan) */
	uint16_t interval = htobs(0x0010);
	uint16_t window = htobs(0x0010);
	uint8_t filter_dup = 0x00; /* Ffilter duplicates (0x00 d don't filter duplicates) */
	int i, dev_id = -1;
	char bad_chars[] = "!@%^*~|";
	char invalid_found = 0;
	
	while ((opt=getopt(argc, argv, "+i:hf:t:n:d")) != -1) {
		switch (opt) {
			case 'i':
				dev_id = hci_devid(optarg);
				if (dev_id < 0) {
					printf("Invalid device\n");
					exit(1);
				}
				break;
			case 'n':
				numSamples = atoi(optarg);
				if (numSamples <= 0 || numSamples > 1000) {
					printf("Invalid number of samples\n");
					exit(1);
				}
				break;
			case 't':
				maxTime = atoi(optarg);
				if (maxTime <= 0 || maxTime > 1000) {
					printf("Invalid timeout\n");
					exit(1);
				}
				break;
			case 'f':
				for (i = 0; i < strlen(bad_chars); ++i) {
					if (strchr(optarg, bad_chars[i]) != NULL) {
						invalid_found = 1;
						break;
					}
				}
				if (invalid_found || strlen(optarg)>255) {
					printf("Invalid file name\n");
					exit(1);
				}
				filename = malloc(strlen(optarg)+1);
				strcpy(filename,optarg);
				break;
			case 'd':
				debug = 1;
				break;
			case 'h':
			default:
				usage();
				exit(0);
		}
	}
	argc -= optind;
	argv += optind;
	optind = 0;

	if (argc < 1) {
		usage();
		exit(0);
	}
	nDevs = argc;

	if (dev_id < 0)
		dev_id = hci_get_route(NULL);

	dd = hci_open_dev(dev_id);
	if (dd < 0) {
		perror("Could not open device");
		exit(1);
	}

	/* clear white list */
	err = hci_le_clear_white_list(dd, 1000);
	if (err < 0) {
		err = -errno;
		fprintf(stderr, "Can't clear white list: %s(%d)\n",
							strerror(-err), -err);
		exit(1);
	}

	/* add BT devices to white list */
	devsBtAddr = malloc(nDevs * sizeof(bdaddr_t));
	for (i=0; i<nDevs; i++) {
		err = str2ba(argv[i], &devsBtAddr[i]);
		if (err < 0) {
			printf("Bad BT address\n");
			exit(1);
		}

		err = hci_le_add_white_list(dd, &devsBtAddr[i], own_type, 1000);
		if (err < 0) {
			err = -errno;
			fprintf(stderr, "Can't add to white list: %s(%d)\n", strerror(-err), -err);
			exit(1);
		}
	}

	err = hci_le_set_scan_parameters(dd, scan_type, interval, window, own_type, filter_policy, 10000);
	if (err < 0) {
		perror("Set scan parameters failed");
		exit(1);
	}

	err = hci_le_set_scan_enable(dd, 0x01, filter_dup, 10000);
	if (err < 0) {
		perror("Enable scan failed");
		exit(1);
	}

	temperature = (int*) malloc(nDevs * sizeof(int));
	humidity = (int*) malloc(nDevs * sizeof(int));
	battery = (int*) malloc(nDevs * sizeof(int));
	ntSamples = (int*) malloc(nDevs * sizeof(int));
	nhSamples = (int*) malloc(nDevs * sizeof(int));
	timeTemperature = malloc(nDevs * sizeof(long));
	timeHumidity = malloc(nDevs * sizeof(long));
	timeBattery = malloc(nDevs * sizeof(long));
	for (i = 0; i < nDevs; i++) {
		temperature[i] = 0;
		humidity[i] = 0;
		battery[i] = 0;
		ntSamples[i] = 0;
		nhSamples[i] = 0;
	}

	err = print_advertising_devices(dd, filter_type);
	if (err < 0) {
		printf("Could not receive advertising events\n");
		exit(1);
	}

	err = hci_le_set_scan_enable(dd, 0x00, filter_dup, 10000);
	if (err < 0) {
		perror("Disable scan failed");
		exit(1);
	}

	hci_close_dev(dd);

	outputValues();
	return 0;
}

// new functions -- assume 

static int scan_active = 0;

int lescan_open(int dev_id) {
    int dd = hci_open_dev(dev_id);

	return dd;
}

void lescan_close(int dd) {
    hci_close_dev(dd);
}



