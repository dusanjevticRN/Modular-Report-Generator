plugins {
    kotlin("jvm") //Appliu-je Kotlin plugin za buildovanje JVM projekta
    id("org.jetbrains.dokka") version "1.8.10" //Dokka je kotlin documentation generator. Cita komentare i koda i kreira html, markdown output
    `java-library` //Mogucnost buildovanja java librarija sa featurima kao sto su odvajanje api=ja implementacija dependensija itd
    `maven-publish` //Dozvoljava pushovanje recimo jar-a na maven repo
}

group = "org.example" //Definise groupID, koji je deo identiteta librarija. Kada npr publishujemo u maven repository, group se koristi za organizovanje librarija
version = "1.0-SNAPSHOT" //Set apujemo verziju naseg libraryja

repositories { //Definisemo gde da searchujemo za nase dependencije
    mavenCentral() //Nas projekat ce skidati dependensije odavde
}

dependencies { //U ovom bloku specificiramo librarije ili module, na koje se nas projekat oslanja. Dependensi se kategorisu na osnovu upotrebe (runtime, compile time i testing). library i modul je tip dependensija
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test { //Test task je odgovoran za pronalazenje i runovanje testova u projektu
    useJUnitPlatform()
}

tasks.javadoc { //ovo je task koji osigurava da se tasks.dokkaJavadoc pokrece
    dependsOn(tasks.dokkaJavadoc) //ova linija osigurava da je dokkaJavadoc task executovan pre nego sto se javadoc task startuje
    doLast { //Kada se javadoc startuje i zavrsi sve se odradi onda printuj ovo
        println("Javadoc task completed with Dokka output.")
    }
}

tasks.dokkaJavadoc { //Ovo je task koji generise dokumentaciju
    outputDirectory.set(file("build/dokka/javadoc")) // Set the output directory
}

//Kada pokrenemo publish task, gradle ce generisati .jar fajl koji sadrzi kompajlovan kod ukljucujuci metadata (groupId, artifact id i verziju)
publishing { //Dozvoljava nasem projektu da generise u publishuje .jar fajlove u repozitorije
    publications {  //Definise razlicite nacine kako nas projeka moze biti publishovan
        create<MavenPublication>("mavenJava") { //Kreiramo novi publication ciji je tip MavenPublication "mavenJava" je ime, mozemo staviti bilo sta tu
            from(components["java"]) // If you're using the 'java' or 'kotlin' plugin

            groupId = "org.example"
            artifactId = "spec"
            version = "1.0.0"
        }
    }
}

kotlin {
    jvmToolchain(21)
}