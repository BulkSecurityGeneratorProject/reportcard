(function() {
    'use strict';
    angular
        .module('reportcardApp')
        .factory('Ecole', Ecole);

    Ecole.$inject = ['$resource'];

    function Ecole ($resource) {
        var resourceUrl =  'api/ecoles/:id';

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
