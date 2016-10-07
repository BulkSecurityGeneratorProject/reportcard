(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EcoleDetailController', EcoleDetailController);

    EcoleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Ecole'];

    function EcoleDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Ecole) {
        var vm = this;

        vm.ecole = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('reportcardApp:ecoleUpdate', function(event, result) {
            vm.ecole = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
