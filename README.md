smartconfig
===
a super extension with [fastconfig](https://github.com/kevin70/fastconfig-maven-plugin)

# Features:

* supply most of the text file
* supply multi mode for replace the text(property, xpath, jsonpath, regex)
* supply reference maven parameters
* supply rename file and directory
* **supply sample javascript judge**

# Install:

* sample:

```xml
    <plugin>
        <groupId>com.github.zdsiyan</groupId>
        <artifactId>smartconfig-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
            <execution>
                <id>config-resources</id>
                <goals>
                <goal>configure</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <config>smart-config.xml</config>
            [<encoding>UTF-8</encoding>]
            [<outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>]
        </configuration>
    </plugin>
```

* include rename:

```xml
	<plugin>
		<groupId>com.github.zdsiyan</groupId>
		<artifactId>smartconfig-maven-plugin</artifactId>
		<version>1.0</version>
		<executions>
			<execution>
				<id>config-resources</id>
				<goals>
					<goal>configure</goal>
				</goals>
				<configuration>
					<config>smart-config.xml</config>
					[<encoding>UTF-8</encoding>]
					[<outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>]
				</configuration>
			</execution>
			
			<execution>
				<id>replace-resources</id>
				<goals>
					<goal>configure</goal>
				</goals>
				<configuration>
					<config>smart-rename.xml</config>
					[<encoding>UTF-8</encoding>]
	      			[<outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>]
				</configuration>
			</execution>
		</executions>
	</plugin>
```

  + **config** configuration file
  + **encoding** target file encoding
  + **outputDirectory** output directory
  
# Configuration file

* sample

```xml
<smart-config>
    <config-file path="test.properties">
      	<pointhandle expression="project_name">${project_name}</pointhandle>
    	<pointhandle expression="static.file">**</pointhandle>
    	<pointhandle expression="abc.ddd">**</pointhandle>
    	<pointhandle expression="web.dir"/>
    </config-file>
    <config-file path="test.xml">
      <pointhandle expression="/server/port">80</pointhandle>
      <pointhandle expression="//host[@id='1']">192.168.1.1</pointhandle>
      <pointhandle expression="//host[@id='2']">192.168.1.2</pointhandle>
      <pointhandle expression="/server/mode/@value">run</pointhandle>
    </config-file>
    <config-file path="xxx\spring-context.xml">
	  	<pointhandle expression="//*[@id='dataSource']" mode="delete"/>
	  	<pointhandle expression="//import[contains(@resource,'cache')]" mode="delete"/>
	  	<pointhandle expression="/beans/*[1]" mode="insert">${node1}</pointhandle>
	  	<pointhandle expression="/beans/*[last()]" mode="insert">${node2}</pointhandle>
	</config-file>
    <config-file path="test.json">
      <pointhandle expression="$.store.book[?(@.author='Evelyn Waugh')].author">Kevin Zou</pointhandle>
      <pointhandle expression="$.store.bicycle.color">${hello.param}</pointhandle>
      <pointhandle expression="$.store.bicycle.price">29.99</pointhandle>
    </config-file>
    <config-file path="test.html" mode="regex">
      <pointhandle expression="&lt;p&gt;(.*?)&lt;/p&gt;">&lt;a&gt;$1__Testing__\\__\$2&lt;/a&gt;</pointhandle>
    </config-file>
</smart-config>
```

* rename and script

```xml
<smart-config>
  <config-file path="xxx\xxx.properties" replace="${project_name}.properties"/>
  <config-file path="xxx" replace="${project_name}-rename-direct" disable="('xxx' == project_name)?true:false"/>
</smart-config>
```

 + config-file instructions
   * path: source file path
   * mode: value(property, xpath, jsonpath, regex). The file type determines the default values.
   * replace: target file/directory name
   * disable: can execute a sample javascript code to judge  whether the config-file is effective
   
 + pointhandle instructions
   * expression: replace expression
   * mode: value(insert, delete) design for xml, and also supply json properties.