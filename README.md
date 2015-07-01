Proof of concept project to do logging through a socket to nxlog

== Wildfly Configuration ==
        <subsystem xmlns="urn:jboss:domain:logging:2.0">
            [...]

            <custom-handler name="NXLOG" class="dk.kyuff.logging.NXLogHandler" module="dk.kyuff.logging">
                    <level name="INFO"/>
                    <properties>
                        <property name="port" value="1514"/>
                        <property name="host" value="localhost"/>
                    </properties>
            </custom-handler>

            [...]

             <root-logger>
                 <level name="INFO"/>
                 <handlers>
                     <handler name="NXLOG"/>
                     [...]
                 </handlers>
             </root-logger>

             [...]
       </subsystem>

== Installation of the Module ==

The Maven project will build a file called logging-handler-${project.version}-wildfly-module.zip

This file needs to be unzipped in $WILDFLY_HOME/modules/system/layers/base/

