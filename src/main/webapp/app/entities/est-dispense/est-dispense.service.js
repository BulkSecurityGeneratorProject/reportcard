(function() {
    'use strict';
    angular
        .module('reportcardApp')
        .factory('EstDispense', EstDispense);

    EstDispense.$inject = ['$resource'];

    function EstDispense ($resource) {
        var resourceUrl =  'api/est-dispenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
