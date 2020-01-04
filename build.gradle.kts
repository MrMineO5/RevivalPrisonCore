/*
 * Copyright (c) 2019. UltraDev
 */

import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

group = "net.ultradev"
version = "1.0.0"

plugins {
    `java-library`
    `kotlin-dsl`
    id("com.github.johnrengelman.shadow") version "4.0.4"
    maven
    kotlin("jvm") version "1.3.20"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://maven.sk89q.com/repo/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("http://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://minevolt.net/repo/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://repo.viaversion.com/")
    maven("https://repo.citizensnpcs.co/")
    maven("http://ci.athion.net/job/FastAsyncWorldEdit/ws/mvn/")
    maven("https://jitpack.io")
    maven("https://mavenrepo.cubekrowd.net/artifactory/repo/")
}

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    // compileOnly("net.citizensnpcs:citizensapi:2.0.22-SNAPSHOT")
    compileOnly("net.citizensnpcs:citizens:2.0.22-SNAPSHOT")
    // compileOnly("fr.minuskube.inv:smart-invs:1.2.6")
    compileOnly("com.sk89q:worldguard:6.1") {
        exclude("org.bukkit", "bukkit")
    }
    compileOnly("net.ess3:EssentialsX:2.17.0") {
        exclude("org.bukkit", "bukkit")
    }
    compileOnly("com.sainttx.holograms:Holograms:2.10.0-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1.4-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-core:6.1.4-SNAPSHOT")
    compileOnly("us.myles:viaversion:2.1.3-SNAPSHOT")
    compileOnly("me.clip:VoteParty:1.14.0")
    compileOnly("com.boydti:fawe-api:latest")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("de.myzelyam:SuperVanish:6.1.0") {
        exclude("net.ess3", "Essentials")
        exclude("be.maximvdw", "MVdWPlaceholderAPI")
    }
    api("org.jetbrains.kotlin:kotlin-stdlib:1.3.20")
    api("org.jetbrains.kotlin:kotlin-stdlib-common:1.3.20")
    implementation("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
}

val fatJar = task("fatJar", type = Jar::class) {
    val include = arrayOf(
        "kotlin-runtime-${getKotlinPluginVersion()}.jar",
        "kotlin-stdlib-${getKotlinPluginVersion()}.jar"
    )

    from(configurations.runtimeClasspath.get()
        .filter { include.contains(it.name) }
        .map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}