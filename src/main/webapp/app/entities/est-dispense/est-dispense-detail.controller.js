(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EstDispenseDetailController', EstDispenseDetailController);

    EstDispenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EstDispense', 'Matiere', 'User'];

    function EstDispenseDetailController($scope, $rootScope, $stateParams, previousState, entity, EstDispense, Matiere, User) {
        var vm = this;

        vm.estDispense = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:estDispenseUpdate', function(event, result) {
            vm.estDispense = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
