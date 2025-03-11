# Wiki Transformer
Simple CLI application written in Java that converts Redmine Wiki pages (Textile format) to MediaWiki format using Pandoc.

## Features
- Fetches a Wiki page from a specified URL.
- Retrieves content in Textile format from a Redmine Wiki instance.
- Prepares the content for further processing.

## Requirements
- Java 21
- Maven
- Pandoc

## Installation
Clone the repository and open it in your preferred IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code with Java support). 

### Running in IDE
1. Open the project in your IDE.
2. Add program arguments in the IDE's run configuration:
   ```
   --url "https://your-redmine-instance/wiki-page"
   ```
  This command fetches the content of the specified Redmine Wiki page and prepares it for conversion.
3. Run the application from the IDE.


## Roadmap
- Add support for automatic conversion to MediaWiki format.
- Implement error handling for invalid URLs and API failures.
- Improve logging and output formatting.

## License
This project is licensed under the MIT License. Feel free to modify and use it.
