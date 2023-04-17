// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

plugins {
  id("java")
  id("org.jetbrains.intellij") version "1.13.3"
}

//group = "org.intellij.sdk"
version = "1.0.2"

repositories {
  mavenCentral()
}
dependencies{
  implementation("org.json:json:20210307")
  // https://mvnrepository.com/artifact/com.h2database/h2
//  implementation("com.h2database:h2:1.3.148")
// https://mvnrepository.com/artifact/org.springframework/spring-jdbc
//  implementation("org.springframework:spring-jdbc:6.0.7")



}
java {
  sourceCompatibility = JavaVersion.VERSION_17
}

// See https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2022.2.5")
}

tasks {
  buildSearchableOptions {
   enabled = false
  }

  patchPluginXml {
    version.set("${project.version}")
    sinceBuild.set("222")
    untilBuild.set("231.*")
  }
}
