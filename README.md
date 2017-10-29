Maven Concrete Version rule
===========================
When releasing a maven based project you should never depend on non-concrete versions.
The standard rules supplied with maven will enforce non-snapshot BUT will allow version ranges.
This project defines an enforcer rule that checks for both SNAPSHOT and Range.

Usage
-----
In order to use this rule you need to 
* add the **maven-enforcer-plugin**.
* configure an execution using the rule as part of the **enforce** goal

Default configuration:

  <concreteVersionRule implementation="com.develeap.enforce.ConcreteVersionRule"/>

Further Configuration

* **checkOnSnapshotBuilds** - (default:false) enforce rule even when building a SNAPSHOT of your project
* **alsoCheckParentVersion** - (default:true) enforce the rule on parent version
* **allowSnapshotVersions** - (default:false) allow SNAPSHOT version in dependencies and parent

An example of a usage pom can be found at usage-pom.xml

Enjoy.