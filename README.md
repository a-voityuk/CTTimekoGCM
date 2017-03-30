# Titanium Android Notifications Module

## Description

- Adds proper support for Google Cloud Messaging (GCM v2).
- Makes your Android App show the count of unread messages as a badge on your App shortcut.

## Usage

Add the module to you project, the latest version of the module can be found inside the dist folder.

**Example:**

<modules>
<module version="2.0.5">ct.timeko.gcm</module>
</modules>


## Changes

**2.0.5**
- Support Titanium sdk 6+

**1.0**
- Initial release

## Usign the module in code

```js
var gcm = require("ct.timeko.gcm");

gcm.registerForPushNotifications({
  senderId: “Enter your sender id”,
  onRegister: onRegisterCallback,
  onMessage: onMessageCallback,
  onError: onErrorCallback
});

function onRegisterCallback(e) {
  console.info("Push token: " + e.token);
}

function onMessageCallback(e) {
  console.info("Push message: " + e.data.payload);
}

function onErrorCallback(e) {
  console.info("Push registration error: " + JSON.stringify(e));
}
```

## License

The MIT License (MIT)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
