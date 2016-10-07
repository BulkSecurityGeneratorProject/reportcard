(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EvaluationDialogController', EvaluationDialogController);

    EvaluationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Evaluation', 'Sequence', 'Eleve', 'AnneeAcademique', 'Matiere'];

    function EvaluationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Evaluation, Sequence, Eleve, AnneeAcademique, Matiere) {
        var vm = this;

        vm.evaluation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sequences = Sequence.query();
        vm.eleves = Eleve.query();
        vm.anneeacademiques = AnneeAcademique.query();
        vm.matieres = Matiere.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.evaluation.id !== null) {
                Evaluation.update(vm.evaluation, onSaveSuccess, onSaveError);
            } else {
                Evaluation.save(vm.evaluation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:evaluationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
