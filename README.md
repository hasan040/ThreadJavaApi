# ThreadJavaApi
We've used json format dataset which contains 5000 data. Since our target is to generate around 600 API calls in 10 seconds so the
*executor.scheduleAtFixedRate()* function will produce the desired output
at constant rate. But if we use *execute.submit()* function it will do all
5000 API calls. Screenshots of 2 different output have been shown below.



![all_five_thousand_calls](https://github.com/hasan040/ThreadJavaApi/assets/54148448/09d4f4df-4108-4e5e-a2bc-e460a4320fc6)

### It's showing about all 5000 calls.

![around_six_hundred_calls](https://github.com/hasan040/ThreadJavaApi/assets/54148448/f753c606-0feb-4869-8db9-d9371dc1dc23)

### It's showing around 600 calls.
