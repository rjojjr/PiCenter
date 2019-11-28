# PiCenter

**_Smart home statistics, control, integration and prediction center._**

This application is built around Raspberry Pi and DHT temperature and humididty sensors to detect 
trends between temperature and furnace/AC usage. The project will eventually
grow to be an IoT smart home command center.

The core of this program will be an analytical engine,
that will calculate various statistics and hopefully make 
accurate predictions, and draw useful conclusions within
a decent realm of certainty.

Initially the program will be built around temperature,
and predicting ways to increase efficiency. We will slowly
add features like lighting and such, and when time allows, 
controlling ductwork and windows.

The application uses MySQL and Spring CRUD repositories to persist data.

The source for the Raspberry Pi application and be found [here](https://github.com/rjojjr/pitemp)

Please feel free to contribute features pertaining to
your own smart home incorporation.

The program is built with Springboot, React, Redux, axios, Scala and py4j
Python Java gateway API to incorporate the Raspberry Pi.

_Branches will be named by toDo-<ToDo number> or bug-<Bug number> or s-<Story number>_





