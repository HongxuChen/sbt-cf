# sbt-cf [![Build Status](https://travis-ci.org/HongxuChen/sbt-cf.svg?branch=master)](https://travis-ci.org/HongxuChen/sbt-cf)

sbt-cf is a naïve sbt plugin that simplifies the usage of the well known [checker-framework](https://checkerframework.org).

# Install

Since it is still a WIP project, only manual installation is supported. 

```bash
git clone git@github.com:HongxuChen/sbt-cf.git
sbt publishLocal
```

# Usage

Add the following line into `project/plugins.sbt` file.

```scala
addSbtPlugin("com.github.hongxuchen" % "sbt-cf" % "0.0.1-SNAPSHOT")
```

