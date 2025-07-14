# Changelog

All notable changes to this project will be documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) and adheres to [Semantic Versioning](https://semver.org/).

## [0.3.0] - 2025-07-14
### Added
- Image download support: detects images on the Redmine Wiki page, downloads them, and saves them to disk.
- Print the absolute path and filename of the converted MediaWiki file after processing.
- Utility method to extract the filename from a given URL.

### Changed
- Filename suffix generation now includes a hash code derived from the source URL to ensure uniqueness.


## [0.2.0] - 2025-04-24
### Added
- New CLI option `--output-dir` allowing users to specify custom output directory for the converted file.
- File names are now automatically sanitized and suffixed with a timestamp for uniqueness.
- Utility methods for filename sanitization and unique suffix generation.

### Changed
- `System.exit()` call was moved from CLI command logic to the Spring Boot `CommandLineRunner` for proper application shutdown.
- Output file location logic refactored and extracted from properties to CLI arguments.

### Fixed
- Application now exits properly when required CLI arguments are missing.
- More detailed error handling and validation of output paths and filenames.

## [0.1.0] - 2025-03-11
### Added
- Initial implementation of CLI argument parsing.
- Fetching Redmine Wiki content using `--url`.
- Convert Textile to MediaWiki and save to local storage.
