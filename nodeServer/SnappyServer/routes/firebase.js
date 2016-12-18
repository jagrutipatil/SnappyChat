var firebase = require('firebase');
var emailModule = require('./email');

var config = {
		  apiKey: "AIzaSyCtNKArzdakLuFNib9p9yprAKi1rLx8kZ4",
		  authDomain: "snappychat-25a5a.firebaseapp.com",
		  databaseURL: "https://snappychat-25a5a.firebaseio.com"
		};

firebase.initializeApp(config);
var firebaseRef = firebase.database().ref().child('timelinecontent');
//var me = "jagpatil22gmailcom";

var getFriendList = function(me, callback) {
	firebase.database().ref('/friends').on("value", function(snapshot) {
		var user = snapshot.child(me).val();
		var email = user.email;
		var friendList = user.friends;		
		
		console.log("my email:" + email);
		console.log("myFriends:" + friendList[0]);
		
		callback(null, friendList);		
	  }, function (errorObject) {
		  console.log("The read failed: " + errorObject.code);
		  callback(errorObject, null);
	  });
}; 		

//var getAllUsers = function(callback) {
//	firebase.database().ref('/users').on("value", function(snapshot) {
//		var userDict = childsnapshot.val();
//		var userArray = [];
//		for (i = 0; i < Object.keys(userDict).length; i++) {
//			userArray.push({
//				key: Object.keys(userDict)[i],
//				email: userDict[Object.keys(userDict)[i]].email});
//		}
//				
//		callback(null, userArray);		
//	  }, function (errorObject) {
//		  console.log("The read failed: " + errorObject.code);
//		  callback(errorObject, null);
//	  });
//}

var notify = function(email, callback) {
	getFriendList(email, function(err, friendList) {
		for (i = 0; i < friendList.length; i++) {
				emailModule.sendEmail(friendList[i], "Your friend: "+ email + " changed profile", function(err, reply) {
					console.log("Notify");		
				});
		}
		callback(null, "Email sent to all friends of " + email);
	});	
};

var isFriend = function(email, callback) {  
	getFriendList( function(err, friendList) {
		if (friendList.indexOf(email) > -1)  {
			callback(true);
		}	else {
			callback(false);
		}
	});
};

exports.onChange = function() {
	firebaseRef.on('child_changed', function(childsnapshot, prevchildname) {  
		console.log("Element Changed");	
		var userDict = childsnapshot.val();
		
		if (Object.keys(userDict).length > 1) {
			var pEmail = userDict[Object.keys(userDict)[0]].emailAddress;
			console.log("Original pEmail: " + pEmail);
			//TODO clean email
			notify("kamlendrasgsitsgmailcom", function(err, reply) {
				console.log(reply);
			});						
		}
	}) ;
}; 