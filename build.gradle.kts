plugins {
    kotlin("jvm") version "1.9.22" //Appliu-je Kotlin plugin za buildovanje JVM projekta
}

group = "org.example" //Definise groupID, koji je deo identiteta librarija. Kada npr publishujemo u maven repository, group se koristi za organizovanje librarija
version = "1.0-SNAPSHOT" //Set apujemo verziju naseg libraryja

repositories { //Specificiramo gde gledamo za dependensije
    mavenCentral() //Maven central repositorijum, online repo sa kog gradle skida stvari
}

dependencies { //Specificiramo externe librarije (dependensije) koje projekat koristi
    testImplementation("org.jetbrains.kotlin:kotlin-test") //Dodajemo dependensi koji je potreban samo za testing
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

//Main build set apuje globalne konfiguracije za ceo projekat