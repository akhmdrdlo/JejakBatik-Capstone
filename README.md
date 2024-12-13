![JejakBatik](https://github.com/user-attachments/assets/dc73a127-aa5e-42b9-9441-49a4ade47e41)
# Welcome to JejakBatik App!!
JejakBatik is a fun and interactive image recognition tool powered by Convolutional Neural Network (CNN) technology. Simply snap a photo of a batik motif, and JejakBatik will instantly identify it, revealing fascinating insights into its philosophical meaning, regional origin, and potential uses. We hope to inspire a new generation of batik enthusiasts by making this rich cultural heritage accessible and engaging.

# Our Team

### Team ID: C242-PS516

| Name                     | Bangkit ID    | Learning Path       | GitHub Link                               | LinkedIn Link |
|--------------------------|---------------|---------------------|-------------------------------------------|---------------|
| Muhammad Gemilang Ramadhan | M004B4KY2856 | Machine Learning   | [GitHub Profile](https://github.com/gemilang-ramadhan)  |  [LinkedIn Profile](https://www.linkedin.com/in/muhammad-gemilang-ramadhan) |
| Uray Fauzan Al Hafizh    | M764B4KY4382  | Machine Learning    | [GitHub Profile](https://github.com/uray03)  |  [LinkedIn Profile](https://www.linkedin.com/in/uray-hafizh-ab74741a2/) |
| Widya Khoirunnisa’       | M312B4KX4475  | Machine Learning    | [GitHub Profile](https://github.com/WidyaKhoirunnisa)| [LinkedIn Profile](https://www.linkedin.com/in/uray-hafizh-ab74741a2/) |
| Akhmad Ridlo Rifa'i      | C547B4NY0290  | Cloud Computing     | [GitHub Profile](https://github.com/akhmdrdlo)| [LinkedIn Profile](https://www.linkedin.com/in/akhmdrdlo/) |
| John Presly Nasution     | C764B4KY2085  | Cloud Computing     | [GitHub Profile](https://github.com/JohnPreslyNasution)   | [LinkedIn Profile](https://www.linkedin.com/in/john-presly-nasution-2310092b7/) |
| Yogi Bastian             | A764B4KY4537  | Mobile Development  | [GitHub Profile](https://github.com/YogiBastian)| [LinkedIn Profile](https://www.linkedin.com/in/yogibastian/) |

# Cloud Computing
### Design Infrastructure Cloud
Our Cloud Architecture used are explain below : 
VM Instance as our main Virtual Machine to run nearly the entire application. Also preserving the VM disk for our Databases.
App Engine to support the main VM, displaying the web pages of some of our features.
Cloud Storage to store all of the images data.
Firestore to store the recent scan histories for each user that’s already registered into our app.
<img src="https://github.com/user-attachments/assets/feaea9df-c4af-4e06-bfe7-c713f7f9957e" style="width:700px"> <br>
# Machine Learning
### Model Architecture
We enhanced the power of the pre-trained EfficientNet B0 model by integrating it with a custom architecture tailored to our specific dataset and objectives. EfficientNetB0 serves as the backbone, while our custom layers balance between the generalization strength of pre-trained features and the adaptability of custom layers, improving classification accuracy and reducing overfitting.

| Layer (type) | Output Shape | Param # |
|---|---|---|
| keras_tensor_1753CLONE (InputLayer) | (None, 7, 7, 1280) | 0 |
| global_average_pooling2d_3 (GlobalAveragePooling2D) | (None, 1280) | 0 |
| batch_normalization_6 (BatchNormalization) | (None, 1280) | 5,120 |
| dropout_6 (Dropout) | (None, 1280) | 0 |
| dense_6 (Dense) | (None, 256) | 327,936 |
| batch_normalization_7 (BatchNormalization) | (None, 256) | 1,024 |
| dropout_7 (Dropout) | (None, 256) | 0 |
| dense_7 (Dense) | (None, 26) | 6,682 |

### Training and Validation Status
<img src="https://github.com/user-attachments/assets/2a592829-49f4-44c3-a4dc-bca71552b46d" style="width:700px">
<img src="https://github.com/user-attachments/assets/523f0f25-d121-40b4-9d02-861bc92c3a66" style="width:700px">

# Mobile Development
### App Mockup and Final Result (App Screenshot)
#### Authentication Feature
<img src="https://github.com/user-attachments/assets/b813462d-2214-4754-b47a-3432220f6092" style="width:500px"><br>
<img src="https://github.com/user-attachments/assets/54fec005-060d-4d0c-aa35-89ffc58b3001" style="width:500px"><br>
<br>#### Main Scan Feature<br>
<img src="https://github.com/user-attachments/assets/596ee8ec-2d05-4286-9aea-33e0b74d706a" style="width:500px"><br>
<img src="https://github.com/user-attachments/assets/49c7ecef-5092-4edf-acae-e75855206baa" style="width:500px"><br>
<br>#### Catalogues and Histories<br>
<img src="https://github.com/user-attachments/assets/43febcf9-245c-45e2-8d11-6cbe68c119ec" style="width:500px"><br>
<img src="https://github.com/user-attachments/assets/5aa47a14-c349-41c2-bc86-a69b197ca669" style="width:500px"><br>
<br>

### App Demonstration
#### Authentication
<img src="https://github.com/user-attachments/assets/595eb30d-1070-4920-be87-d859f3e32b07" style="width:1000px"><br>

#### Scanner Feature
<img src="https://github.com/user-attachments/assets/cea04df7-9432-4739-936d-e961a01f06b0" style="width:1000px"><br>

#### Full App Demo <a href="https://drive.google.com/file/d/1zCOZtRKug1x-G7iNuYUKel_gFCkdudKn/view?usp=drive_link" target="_blank">Link!!</a>

# How to use our app? Simple!! Just download the .apk file on this <a href="https://drive.google.com/file/d/1Jzn7D0IQHKWbxBqfz8PizLCoHnbr476p/view?usp=drive_link" target="_blank"> Link!! </a>


