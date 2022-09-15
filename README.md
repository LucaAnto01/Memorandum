# Memorandum 📝
---
<h4>📱Android application designed for Memo management</h4>

---

<h3>📁Folders structure</h3>

<ul>

  <li>activities</li>
  <ul>
    <li>Application activities are contained here</li>
  </ul>
  
  <li>models</li>
  <ul>
    <li>The models necessary for the abstraction of the application are present here: Location & Memo</li>
  </ul>
  
  <li>services</li>
  <ul>
    <li>Services offered and required for the application are here: Geo fencing managing, Geo fencing broadcast reciver, Location managing, Notification managing</li>
  </ul>
  
  <li>utils</li>
  <ul>
    <li>Utils classes can be found here: Permission manager, Recycler View Adapter, Recycler View touch adapter, Memo app data</li>
  </ul>
  
</ul>

<div><p>Everything related to style was managed in the various subfolders of <i>res</i></p></div>

---

<h3>🪞Application fragments</h3>

<ul>

  <li>All</li>
  <ul>
    <li>All Memo are here: expired, completed and valid. Sorted by date</li>
  </ul>
  
  <li>Memo</li>
  <ul>
    <li>Here there are only the valid Memo. Sorted by date</li>
  </ul>
  
  <li>Map</li>
  <ul>
    <li>Memo with an associated position and valid are displayed here in a map</li>
    <li>Click on the Memo-pin 📗 to see to display more information such as title and description</li>
  </ul>
  
</ul>

---

<h3>🤳🏼Application usage</h3>

<div><p>When application start, the necessary permissions will be requested. In order for the application to function optimally, it will be necessary to grant the use of the location in background, to do this: settings --> applications --> Memorandum --> location: always grant.<br>
Geo-fences are loaded on application start-up.<br>
To edit a Memo, drag it to right.<br>
To delete a Memo, long press on it.<br>
A notification with title and description is generated following activation of the geofence (Due to unexplained error --> through position)</p></div>

---

<div>
  <img src="https://github.com/devicons/devicon/blob/master/icons/android/android-plain.svg" title="Android" alt="Android" width="40" height="40"/>&nbsp;
  <img src="https://github.com/devicons/devicon/blob/master/icons/java/java-original.svg" title="Java" alt="Java" width="40" height="40"/>&nbsp;
  <img src="https://github.com/devicons/devicon/blob/master/icons/google/google-original-wordmark.svg" title="Google" alt="Google" width="40" height="40"/>&nbsp;
  <img src="https://github.com/devicons/devicon/blob/master/icons/git/git-original-wordmark.svg" title="Git" **alt="Git" width="40" height="40"/>
</div>
