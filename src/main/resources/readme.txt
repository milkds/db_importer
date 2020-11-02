Step 1:
- Set Prod Item fields: partNo, Manufacturer, ItemType
Step 2:
- Set ItemPics
* Must not be null. Invalid urls will be skipped at pic downloading method, later.
* For each ItemPic - set url and Item.
Step 3:
- Set ShockParams
*If no parameters present - make blank object, as we will need it in future - to check which info is absent and needs to be filled
*Link prodItem and shockParams to each other. (call two setters)
Step 4:
- Set ItemAttributes
*Check all available fields for item, choose which are for item and put in item attributes.
Step 5:
- Set fitments
Step 5.1
- Build production cars