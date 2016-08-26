(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EleveDetailController', EleveDetailController);

    EleveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Eleve', 'Evaluation', 'Inscrire'];

    function EleveDetailController($scope, $rootScope, $stateParams, previousState, entity, Eleve, Evaluation, Inscrire) {
        var vm = this;

        vm.eleve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:eleveUpdate', function(event, result) {
            vm.eleve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
