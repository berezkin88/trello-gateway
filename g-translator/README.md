How to create appsScript:
1. Create new script with such code `appsScript.js` on https://www.google.com/script/start/
2. Click Publish -> Deploy as webapp -> Who has access to the app: Anyone even anonymous -> Deploy. And then copy your web app url, you will need it for calling translate
3. Copy appsScript URL and put it to application.yml in property `g-transaltor.url`

Usage:

```java
gTranslatorService.translate("Донат на ремонт авто для 95 бригади ");
```