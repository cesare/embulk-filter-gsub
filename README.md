# Gsub filter plugin for Embulk

Embulk filter plugin to convert text column values with regular expressions.

## Overview

* **Plugin type**: filter

## Configuration

- **target_columns**: columns to convert text value (array, default: `[]`)

### target column configuration

- **type**: type of text substitution (string, one of `regexp_replace`, `to_lower_case`, `to_upper_case`, default: `regexp_replace`)

#### regexp_replace

- **pattern**: regular expression pattern to be substituted
- **to**: replacement string

##### Example

```yaml
target_columns:
  foo:
    - type: regexp_replace
      pattern: "(\w*):\s*(\w*)"
      to: "$1 = [$2]"
```

#### to_lower_case

- **pattern**: regular expression pattern to be substituted. optional; if omitted, whole text is converted into lower case letters

##### Example

```yaml
target_columns:
  foo:
    - type: to_lower_case
```

#### to_upper_case

- **pattern**: regular expression pattern to be substituted. optional; if omitted, whole text is converted into upper case letters

##### Example

```yaml
target_columns:
  foo:
    - type: to_upper_case
```

### Multiple conversion

You can apply multiple conversion on a column value.

```yaml
target_columns:
  foo:
    - type: regexp_replace
      pattern: "</?\w*\s*/?>"
      to: ""
    - type: regexp_replace
      pattern: "(\w*):\s*(\w*)"
      to: "$1 = [$2]"
```

## Example

```yaml
filters:
  - type: gsub
    target_columns:
      foo:
        - type: regexp_replace
          pattern: "(\w*):\s*(\w*)"
          to: "$1 = [$2]"
      bar:
        - type: to_lower_case
      baz:
        - type: to_upper_case
          pattern: "test"
```


## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```
