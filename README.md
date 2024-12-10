The Modular Report Generator is a flexible and extensible system for generating reports in various formats, including CSV, TXT, PDF, and Excel. The project uses the Service Provider Interface (SPI) for a modular design, allowing seamless integration of new report types and functionalities.

Key features include:
Dynamic Calculations: Supports operations such as COUNT, SUM, and AVERAGE on report data.
Multi-Format Support: Generate reports in CSV, custom TXT, PDF (via HTML conversion), and Excel (using Apache POI).
 · Report Customization: Add headers, summaries, and select between formatted or unformatted content.
 · Documentation with Dokka: Integrated Dokka to generate clear documentation for interfaces and implementations, making it easier to understand and extend the system’s components.
 · Command-Line Interface (CLI): Generate reports from database queries via the command line.
 · Build Automation: Managed with Gradle for efficient dependency management and packaging.

Technologies Used:
 · Kotlin
 · Service Provider Interface (SPI)
 · Apache POI (Excel report generation)
 · Dokka (automated documentation)
 · HTML-to-PDF Conversion
 · Gradle
 · Command-Line Interface (CLI)
