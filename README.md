# Gsub filter plugin for Embulk

Embulk filter plugin to convert text column values with regular expressions.

## Overview

* **Plugin type**: filter

## Configuration

- **target_columns**: columns to convert text value (array, default: `[]`)

### target column configuration

- **type**: type of text substitution (string, default: `regexp_replace`)

Supported Types are:

* regexp_replace
* to_lower_case
* to_upper_case

#### regexp_replace

- **pattern**: regular expression pattern to be substituted (string, required)
- **to**: replacement string (string, required)

##### Example

```yaml
target_columns:
  foo:
    - type: regexp_replace
      pattern: "(\w*):\s*(\w*)"
      to: "$1 = [$2]"
```

it converts input like this

foo | bar
-----|-----
example-foo: 1234 | example-bar: 9876

into the output

foo | bar
-----|-----
example-foo = [1234] | example-bar: 9876

#### to_lower_case

- **pattern**: regular expression pattern to be substituted (string, optional)

If `pattern` is omitted, whole text is converted into lower case letters.

##### Example

```yaml
target_columns:
  foo:
    - type: to_lower_case
```

it converts input like this

foo | bar
-----|-----
ABC | XYZ

into the output

foo | bar
-----|-----
abc | XYZ

#### to_upper_case

- **pattern**: regular expression pattern to be substituted (string, optional)

If `pattern` is omitted, whole text is converted into upper case letters.

##### Example

```yaml
target_columns:
  foo:
    - type: to_upper_case
```

it converts input like this

foo | bar
-----|-----
abc | xyz

into the output

foo | bar
-----|-----
ABC | xyz

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
