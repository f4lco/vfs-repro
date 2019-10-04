plugins {
  java
}

apply(plugin = "java")

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.named<Test>("test") {
  useJUnitPlatform()
  testLogging {
    events("passed", "failed")
  }
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.apache.commons:commons-io:1.3.2")
  testImplementation("org.apache.commons:commons-vfs2:2.4.1")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}
