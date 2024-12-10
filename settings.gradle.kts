plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "KomponenteProjekatPrvi" //Referise na root projekat, kada prkrenemo gradlew orikazace ovo ime kao mani projekt name
include("spec") //Na ovaj nacin govorimo gradle-u da inkluduje ove subprojekte u ceo taj multi-module projekat odnosno build
include("prvaImpl")
include("test")
include("drugaImpl")
include("drugaImplm")
include("testApp")
include("trecaImpl")
include("cetvrtaImpl")

//Konfigurisemo overall Gradle build za multi-module projekte