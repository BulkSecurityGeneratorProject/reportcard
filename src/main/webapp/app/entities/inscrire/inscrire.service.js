(function() {
    'use strict';
    angular
        .module('reportcardApp')
        .factory('Inscrire', Inscrire);

    Inscrire.$inject = ['$resource'];

    function Inscrire ($resource) {
        var resourceUrl =  'api/inscrires/:id';

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
