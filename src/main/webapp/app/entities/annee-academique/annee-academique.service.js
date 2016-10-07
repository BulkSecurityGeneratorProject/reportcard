(function() {
    'use strict';
    angular
        .module('reportcardApp')
        .factory('AnneeAcademique', AnneeAcademique);

    AnneeAcademique.$inject = ['$resource'];

    function AnneeAcademique ($resource) {
        var resourceUrl =  'api/annee-academiques/:id';

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
