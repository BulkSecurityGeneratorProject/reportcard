(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('AnneeAcademiqueDialogController', AnneeAcademiqueDialogController);

    AnneeAcademiqueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AnneeAcademique', 'Evaluation', 'Inscrire', 'EstDispense'];

    function AnneeAcademiqueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AnneeAcademique, Evaluation, Inscrire, EstDispense) {
        var vm = this;

        vm.anneeAcademique = entity;
        vm.clear = clear;
        vm.save = save;
        vm.evaluations = Evaluation.query();
        vm.inscrires = Inscrire.query();
        vm.estdispenses = EstDispense.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.anneeAcademique.id !== null) {
                AnneeAcademique.update(vm.anneeAcademique, onSaveSuccess, onSaveError);
            } else {
                AnneeAcademique.save(vm.anneeAcademique, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:anneeAcademiqueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
