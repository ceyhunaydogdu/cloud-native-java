var app=angular.module("app", [])

app.factory('oauth', function () {
    return {details:null, name:null, token:null};
});

app.run([
    '$http', '$rootScope', 'oauth', function  ($http, $rootScope, oauth) {
        $http.get("/reservations/user").success(function (data) {
            oauth.details=data.userAuthentication.details;
            oauth.name=oauth.details.name;
            oauth.token=data.details.tokenValue;

            $http.defaults.headers.common['Authorization']='bearer '+oauth.token;
            $rootScope.$broadcast('auth-event', oauth.token);
        });
    }
]);

app.controller("home", function ($http, $rootScope, oauth) {
    var self=this;
    self.authenticated=false;

    $rootScope.$on('auth-event', function (evt, ctx) {
        self.user=oauth.details.name;
        self.token=oauth.token;
        self.authenticated=true;

        // var name=window.prompt('Who would you like to greet?');

        $http.get('reservation-service/message')
            .success(function (data) {
                self.greeting=data;
            })
            .error(function (e) {
                console.log('Hata..: '+JSON.stringify(e));
            });
    });
});