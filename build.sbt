name := "Sound Tests"
 
version := "1.0"
 
scalaVersion := "2.9.1"
 
// let's me specify which "main" i want to run
// see  https://github.com/harrah/xsbt/wiki/Quick-Configuration-Examples
// mainClass in (Compile, run) := Some("foo.TestMp3")
mainClass in (Compile, run) := Some("foo.TestJavaSound")

