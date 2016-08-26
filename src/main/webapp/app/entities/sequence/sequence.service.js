(function() {
    'use strict';
    angular
        .module('reportcardApp')
        .factory('Sequence', Sequence);

    Sequence.$inject = ['$resource'];

    function Sequence ($resource) {
        var resourceUrl =  'api/sequences/:id';

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
