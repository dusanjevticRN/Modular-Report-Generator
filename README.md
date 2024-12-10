The Modular Report Generator is a flexible and extensible system for generating reports in various formats, including CSV, TXT, PDF, and Excel. The project uses the Service Provider Interface (SPI) for a modular design, allowing seamless integration of new report types and functionalities.

It supports dynamic calculations such as COUNT, SUM, and AVERAGE on report data. Users can generate reports in multiple formats, including CSV, custom TXT, PDF (via HTML conversion), and Excel (using Apache POI).
The system allows report customization by adding headers, summaries, and selecting between formatted or unformatted content.

To facilitate documentation, Dokka is integrated for generating clear documentation of interfaces and implementations, making the system easier to understand and extend. The project also features a Command-Line Interface (CLI) for generating reports from database queries. Gradle is used for build automation, ensuring efficient dependency management and packaging.

The primary technologies used include Kotlin, SPI, Apache POI, Dokka, HTML-to-PDF conversion, and CLI tools.
