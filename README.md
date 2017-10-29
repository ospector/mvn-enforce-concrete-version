Maven Enforcer rule for windows executables
===========================================
This simple project adds a custom enforcer rule that can be applied when your maven based build relies on windows executables and you want to make sure that you are using the correct version.

Usage
-----
In order to use this rule you need to 
* add the **maven-enforcer-plugin**.
* configure an execution using the rule as part of the **enforce** goal

Rule Configuration:

                                <exeVersionRule implementation="com.omrispector.maven.enforce.ExeVersionRule">
                                    <component>Excel</component>
                                    <checkOn>C:\Program Files (x86)\Microsoft Office\Office12\excel.exe</checkOn>
                                    <version>[12.0,12.2)</version>
                                </exeVersionRule>

**component** - Name of component for user friendly error if rule fails

**checkOn** - Full path to DLL or executable to be checked

**version** - a version range to be validated

Following is a full example limiting the version of Excel:

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-enforcer-plugin</artifactId>
                <version>1.2</version>
                <dependencies>
                    <dependency>
                        <groupId>com.omrispector.mvn.enforce</groupId>
                        <artifactId>exe-version</artifactId>
                        <version>1.0</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>enforce</id>
                        <configuration>
                            <rules>
                                <exeVersionRule implementation="com.omrispector.maven.enforce.ExeVersionRule">
                                    <component>Excel</component>
                                    <checkOn>C:\Program Files (x86)\Microsoft Office\Office12\excel.exe</checkOn>
                                    <version>[12.0,12.2)</version>
                                </exeVersionRule>
                            </rules>
                        </configuration>
                        <goals>
                            <goal>enforce</goal>
                        </goals>

                    </execution>
                </executions>
            </plugin>

Version Semantics
-----------------
The version semantics are taken from http://maven.apache.org/enforcer/enforcer-rules/versionRanges.html

Enjoy.