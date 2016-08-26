(function() {
    'use strict';
    angular
        .module('reportcardApp')
        .factory('Eleve', Eleve);

    Eleve.$inject = ['$resource', 'DateUtils'];

    function Eleve ($resource, DateUtils) {
        var resourceUrl =  'api/eleves/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datenaissance = DateUtils.convertLocalDateFromServer(data.datenaissance);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.datenaissance = DateUtils.convertLocalDateToServer(data.datenaissance);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.datenaissance = DateUtils.convertLocalDateToServer(data.datenaissance);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
