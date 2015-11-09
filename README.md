# PsychoQuest
PsychoQUest is a an application that provides a tablet implementation of two psychological questionnaires: the Self-assessment Manikin (SAM) and the NASA-TLX.
The application allows you to:
- Create an experiment that can be divided into multiple sessions (e.g., if you need your participants to fill the questionnaires multiple times for different experimental conditions)
- Assign participant to an experiment
- Choose whether you want to use the SAM questionnaire only, the NASA-TLX questionnaire only, or both of them in a single experiment
- Save your participants' results on the tablet SD card or on Dropbox (for the Dropbox integration, you will need to have a Dropbox account)

## Self-assessment Manikin
The Self-assessment Manikin is used to collect an participants’ self-reported
affective state (Lang, 1980). The affective state is measured with two scales: valence and arousal (the dominance scale is not implemented in this version of the SAM questionnaire). In the
version of the SAM questionnaire implemented in this application, each scale is composed of 5 figures and 4 inter-points (i.e., a participant can select a value of valence or arousal between two figures).
Each picture in the valence and arousal scale represents a value of valence or arousal, respectively. The
top-most picture represents the lowest level, and the bottom-most picture represents the highest level of
valence or arousal that can be chosen by the participant.

## NASA-TLX
The NASA-TLX questionnaire is used to study the workload of participants. The NASA-TLX questionnaire is a well known, highly used questionnaire that measures the workload divided into six scales:
- Mental Demand
- Physical Demand
- Temporal Demand
- Performance
- Effort
- Frustration

Each scale is represented by a number between 0 (low) and 100 (high). The final raw score of the NASA-TLX questionnaire is the average over the six scales. 

In the original version of the NASA-TLX, there is a second part of the questionnaire which requires the participants to weight each scale according to their importance. In the version implemented in this application, this weight attribution is not implemented. Many studies do not use this weight attribution part because it is time consuming.

## Limitations
This application has been implemented for my own research. It doesn't have the aim to provide a professionnal, bug-free application. Hence, there are a few limitations:
- The app will render correctly only on tablet devices (i.e., the UI will be buggy on smartphone, or on devices whose screens are smaller than 10")
- The app runs on portrait orientation
- Though the application has been used for my own research, I do not guarantee that it is bug-free. I provide this applicatoin "as-is". Therefore, you should use this application at your own risks (e.g., data-loss, data-corruption could happen)
- The app has been tested only on one real device -- a Samsung Galaxy TAB 10.1 running Android API 15. Though it's been implemented to work with API level 11 and higher, I can not guarantee it.

## Your feedback is important!
If you want to use, or test the application, please do not hesitate to either build the application from the source, or to contact me to know how you can install it on your device!

I haven't yet uploaded the application on the official Android App Store because I would like to perform more tests before doing so. Your help (e.g., test the app) would, therefore, be very appreciated!!

## Feature requests
I will be happy to read your feature requests! Please, do not hesitate to contact me. We'll see together what is possible to do.
## How to install 
In order to install this app on your table device, you will need to clone the repo and import the project in Android Studio. Once the import is done, you will need to create a Dropbox API key in the Dropbox API console. Please follow the instructions here: https://www.dropbox.com/developers-v1/core/sdks/android
When you have the project in Android Studio, you will have some errors related to a file "dropbox_keys.xml" file that does not exist. Create this file in 'app/src/main/res/values/' with the following content:

<pre>
<code>
&lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;resources&gt;
    &lt;string name="dropbox_private_key">YOUR_PRIVATE_KEY&lt;/string&gt;
    &lt;string name="dropbox_public_key">YOUR_PUBLIC_KEY&lt;/string&gt;

&lt;/resources&gt;
</code>
</pre>

Please note that if you contact me to install the app directly on your device, you won't need to go through these steps.

## License
This software is distributed under the MIT license
