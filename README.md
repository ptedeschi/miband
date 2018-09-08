# Mi Band

This Android project is all about handling notification for Mi Band 3.
The third generation of the Xiaomi fitness bracelet, the Mi Band 3, is an awesome and afordable device. It works great with it's own app, the Mi Fit, but I miss some customization regarding the notifications.

As the space to display text on the band is limited, every character counts. So this project aims to work on it using the following changes:
- display first name only
- remove spaces on group names
- remove emojis, accents and special characters
- ignore video and photo messages 

## Getting Started

By now, all you need to do is insert mannually your Mi Band 3 Bluetooth address at miband/src/app/src/main/java/br/com/tedeschi/miband/mechanism/ProcessNotification.java

```
MiBand miband = new MiBand();
miband.connect(context, "<ENTER-YOUR-MIBAND-ADDRESS");
```

## Supported apps

* WhatsApp
* Gmail

## Contributors & Info Sources

This project was inspired by [Gadgetbridge](https://github.com/Freeyourgadget/Gadgetbridge), an astonishing application that really helped me to encode the message to be sent to Mi Band device.
