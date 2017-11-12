# DNS Client written in Java

## Description
Can make requests for A (IP addresses), MX (mail server), and NS (name server) records

Query structure:

`Java DnsClient [-t timeout] [-r max-retries] [-p port] [-mx|-ns] @server name`

- timeout(optional) gives how long to wait, in seconds, before retransmitting an unanswered query. Default value: 5.
- max-retries(optional) is the maximum number of times to retransmit an unanswered query before giving up. Default value: 3.
- port(optional) is the UDP port number of the DNS server. Default value: 53.
- -mx or -ns flags (optional) indicate whether to send an MX (mail server) or NS (name server) query. At most one of these can be given, and if neither is given then the client will send a type A (IP address) query.
 server(required) is the IPv4 address of the DNS server, in a.b.c.d. format
- name(required) is the domain name to query for.


## Examples

`Java DnsClient @132.206.85.18 www.mcgill.ca`

`Java DnsClient –t 10 –r 2 –mx @8.8.8.8 mcgill.ca`
