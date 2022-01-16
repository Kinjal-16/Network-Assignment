# Network-Assignment
1.	In this network, I have assumed one Publisher creating as many topics as it wants to in order to publish a message to one these topics which is then routed to the one or more subscribers subscribed to this topic via the Broker.  I have also assumed that the one publisher is a teacher, and the many subscribers are his students for the purpose of demonstration. 

2.	At first the publisher, creates a new topic which is added to a hash map. As soon as the topic is created it is sent in a packet via a broker to the subscribers which then can add this topic to its own hash table of topics. Multiple number of topics can be created by the publisher. The subscribers can subscribe to one or more of these topics and can receive the relevant messages when messages are published to these topics. The subscriber also maintains a list of these subscribed topics. 


3.	The publisher when publishes a message on one of these topics, a packet is created, and this packet is routed via the broker to the subscriber. This network implements Client-side filtering, i.e., the broker sends the packet to all the subscribers listening on the same port and IP address using multicasting. The subscriber decides which messages in these packets to read from using its list of subscribed topics and accordingly send an acknowledgement to the publisher, again via the broker.


4.	The subscribers can also send messages to the publisher via the broker.

5. To run this project, open the src folder and run one instance of Server.java, One instance of User.java and one or more instances of dashboard.java where User.java is the publisher, dashboard.java is the subscriber and Server.java is the broker in this project. These programs can also be run on separate docker containners but make sure you are running a X11 display server like Xming in the background.
