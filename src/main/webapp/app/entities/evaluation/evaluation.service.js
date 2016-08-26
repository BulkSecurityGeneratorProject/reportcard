(function() {
    'use strict';
    angular
        .module('reportcardApp')
        .factory('Evaluation', Evaluation);

    Evaluation.$inject = ['$resource'];

    function Evaluation ($resource) {
        var resourceUrl =  'api/evaluations/:id';

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
