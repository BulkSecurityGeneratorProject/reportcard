(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EcoleDetailController', EcoleDetailController);

    EcoleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ecole'];

    function EcoleDetailController($scope, $rootScope, $stateParams, previousState, entity, Ecole) {
        var vm = this;

        vm.ecole = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:ecoleUpdate', function(event, result) {
            vm.ecole = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
