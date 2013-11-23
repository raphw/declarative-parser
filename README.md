A declarative parser based on Java beans
---------------------
This miniature tool provides help with parsing a custom content format by creating Java beans that extract this data
where each bean resembles a single row of content of a specified source. The mapping of the input to a Java bean is based
on regular expressions what allows great flexibility. However, the syntax of regular expressions is enhanced by
properties expressions that allow the direct mapping of bean properties within this regular expression describing these
contents.

This tool is intended to be as light-weight as possible and comes without dependencies. It is however quite extensible as
demonstrated below.

#### Simple example
The tool is used by a single entry point, an instance of `BeanTransformer` which can read content for a specified bean. By
doing so, the parser only needs to build patterns for a specified bean a single time. Therefore, this tool performs equally
good as writing native content parsers, once it is set up.

As an example for the use of this tool, imagine you want to read in data from some sample file *sample.txt* containing
the following data in an imaginary format:

```
##This is a first value##foo&&2319949,
##This is a second value##bar&&741981,
##This is a third value##&&998483,
```

This tool would now allow you to directly translate this file by declaring the following Java bean:

```
@MatchBy("##@intro@##@value@&&@number@,")
class MyBean {

    private String intro;

    @OptionalMatch
    private String value;

    private int number;

    // Getters and setters...
}
```

The `@name@` expressions each represent a property in the bean which will be filled by the data found at this particular point
within the regular expression. The expression can be escaped by preceeding the **first** `@` symbol with backslashes as for
example `\\@name@`. A back slash can be escaped in the same manner.

With calling `BeanTransformer.make(MyBean.class).read(new FileReader("./sample.txt"))` you would receive a list of `MyBean`
instances where each instance resembles one line of the file. All properties would be matched to the according property name
that is declared in the bean.

#### Matching properties
The `@MatchBy` annotation can also be used for fields within a bean that is used for matching. This tool will normally discover
a pattern by interpreting the type of a field that is referenced in the expression used for parsing the content. Since the
`@number@` expression in the example above references a field of type `int`, the field would be matched against the regular expression
`[0-9]+`, representing any non-decimal number. All other primitive types and their wrapper types are also equipped with a
predefined matching pattern. All other types will by default be matched by a **non-greedy** match-everything pattern. After
extracting a property, the type of the property will be tried to be instantiated by:

1. Invoking a static function with the signature `valueOf(String)` defined on the type.
2. Using a constructor taking a single `String` as its argument.

This default behavior can however be changed. A field can be annotated with `@ResolveMatchWith` which requires a subtype of
a `PropertyDelegate` as its single argument. An instance of this class will be instantiated for transforming expressions from
a string to a value and revers. The subclass needs to override the only constructor of `PropertyDelegate` and accept the same
types as this constructor.

An optional match can be declared by annotating a field with `@OptionalMatch`. If no match can be made for such a field, the
`PropertyDelegate`'s setter will never be invoked (when using a custom `PropertyDelegate`).

#### Dealing with regular expressions
Always keep in mind that `@MatchBy` annotation take regular expressions as their arguments. Therefore, it is important to escape
all special characters that are found in regular expressions such as for example `.\\*,[](){}+?^$`. Also, note that the
default matching patterns for non-primitive types or their wrappers is **non-greedy**. This means that the pattern `@name@`
would match the line **foo** by only the first letter **f**. If you want to match the full line, you have to declare the matching
expression as `^@name@$` what is the regular expression for a full line match. Be aware that using regular expressions might
require you to define a `@WritePattern` which is described below.

Using regular expressions allows you to specify matching constraints natively. Annotating a field with `@MatchBy("[a-z]{1,5]")`
would for example allow for only matching lines where the property is represented by one to five lower case characters.
Configuring mismatch handling is described below.

#### Writing beans
Similar to reading contents from a source, this utility allows to write a list of beans to a target. Without further
configuration, the same pattern as in `@MatchBy` will be used for writing where the property expressions are substituted by
the bean values. This can however result in distorted output since symbols of regular expressions are written *as they are*.
Therefore, a user can define an output pattern by declaring `@WritePattern`. This pattern understands the same type of property
expressions such as `@name` but does not use regular expressions. Remember that regular expressions must therefore not be escaped
when a `@WritePattern` is specified. A property expression can however be escaped in the same manner as in a `@MatchBy` statement.

#### Handling mismatches
When a content source is parsed but a single line cannot be matched to the specified expression, the extraction will abort
with throwing a `TransformationException`. Empty lines will however be skipped. This behavior can be configured by declaring
a policy within `@Skip`. That way, a non-matched line can either ignored, throw an exception or be ignored for empty lines. An
empty line a the end of a file is always ignored.

#### Builder
The `BeanTransformer` offers a builder which allows to specify different properties. Mostly, it allows to override properties
that were declared in a specific bean such as the content pattern provided by `@MatchBy`, a `@Skip` policy or the `@WritePattern`.
This allows the reuse of a bean for different content sources that contain the same properties but differ in their display. Also,
this allows to provide a pattern at run time.

#### Performance considerations
All beans are constructed and accessed via field reflection. (It is therefore not required to define setters and getters for beans.
A default constructor is however required. In the process, primitive types are accessed as such and not wrapped in order to
avoid such overhead. Java reflection is usually considered to be slower than conventional access. However, modern JVMs such as
the HotSpot JVM are efficient in detecting the repetitive use of reflection  and compile such access into native byte code.
Therefore, this tool should not perform worse than a hand-written matcher once the `BeanTransformer` is set up.

#### Extension points
Besides providing your own `PropertyDelegate`s, it is possible to implement a custom `IDelegationFactory` which is responsible
for creating (custom) `PropertyDelegate`s for any field. The default implementation `SimpleDelegationFactory` provides an example 
for such an implementation. That way, it would be for example possible to automatically create suitable patterns for [bean 
validation (JSR-303) annotations](http://beanvalidation.org/1.0/spec/).

[![Build Status](https://travis-ci.org/raphw/declarative-parser.png)](https://travis-ci.org/raphw/declarative-parser)
