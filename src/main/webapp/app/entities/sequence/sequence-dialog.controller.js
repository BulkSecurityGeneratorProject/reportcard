(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('SequenceDialogController', SequenceDialogController);

    SequenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sequence', 'Evaluation'];

    function SequenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sequence, Evaluation) {
        var vm = this;

        vm.sequence = entity;
        vm.clear = clear;
        vm.save = save;
        vm.evaluations = Evaluation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sequence.id !== null) {
                Sequence.update(vm.sequence, onSaveSuccess, onSaveError);
            } else {
                Sequence.save(vm.sequence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:sequenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
