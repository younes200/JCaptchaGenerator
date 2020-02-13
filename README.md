

## Captcha Generator

Captcha generator based on [JCaptcha](https://jcaptcha.atlassian.net/) used CNN training.
The repo includes modified version of JCaptcha to customise generators, fill free to do the same ;)

## RUN:

    ./gradle build

After build

    ./gradle run

## Usage

    usage: 
    -f,--folder <arg>       output folder, default to samples/
    -n,--count <arg>        count of captcha to generate, default to 20000
    -r,--repetition <arg>   Combinaition repetition, default to 10
