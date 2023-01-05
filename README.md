# xjc-equals-plugin

immutable-xjc is an XJC plugin for JAXB 2.x and 3.x that adds hashCode and equals methods to generated models.

## Versions
The plugin is built for JDK 11+, but should work with JDK 1.8. Supported JAXB versions are 2.x and 3.x.

## XJC options
**-generateEquals**

The plugin provides only a single option **-generateEquals**. To enable this, the plugin's jar file needs to be added to the XJC classpath.

## Release notes
**0.1**
- initial release of the plugin

## Attributions
This code was mainly derived from the [immutable-xjc](https://github.com/sabomichal/immutable-xjc) Plugin by Michal Sabo. Especially the maven configuration as well as the test resources are very similar to the ones in that project.