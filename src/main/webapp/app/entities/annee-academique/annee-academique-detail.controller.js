(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('AnneeAcademiqueDetailController', AnneeAcademiqueDetailController);

    AnneeAcademiqueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AnneeAcademique', 'Evaluation', 'Inscrire', 'EstDispense'];

    function AnneeAcademiqueDetailController($scope, $rootScope, $stateParams, previousState, entity, AnneeAcademique, Evaluation, Inscrire, EstDispense) {
        var vm = this;

        vm.anneeAcademique = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:anneeAcademiqueUpdate', function(event, result) {
            vm.anneeAcademique = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
