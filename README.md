[![Build Status](https://travis-ci.org/ohumbel/klingklong.svg)](https://travis-ci.org/ohumbel/klingklong)
[![Code Coverage](https://img.shields.io/codecov/c/github/ohumbel/klingklong/master.svg)](https://codecov.io/github/ohumbel/klingklong?branch=master)
[![Apache License](https://img.shields.io/badge/license-Apache%202.0-orange.svg)](https://github.com/ohumbel/klingklong/blob/master/LICENSE)
[![Supported Versions](https://img.shields.io/badge/Java-8%2C%2011-blue.svg)](https://travis-ci.org/ohumbel/klingklong)

![codecov.io](https://codecov.io/github/ohumbel/klingklong/branch.svg?branch=master)



# klingklong
Message communication between two JVM processes, using sockets.

This is a running prototype.
Next steps would be to provide a good reference how to use it.


----

Web sockets are considered as overengineering for this purpose. But if you are interested in a small demo, you can use the `DocServer` and `DocClient` on the `web.socket.demo` branch for reference / as starting point.


----

## TODO
 - Real life test by using two child processes (jvm's)
 - Is it possible to encrypt/decrypt the messages?
 - Rename properties with the package prefix, to be sure they do not conflict with other system properties
 - Implement and test the property hierarchy (VM arguments, user home, ours)
 - Implement checks for invalid configurations
 