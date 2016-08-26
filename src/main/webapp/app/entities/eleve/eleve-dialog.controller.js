(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EleveDialogController', EleveDialogController);

    EleveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Eleve', 'Evaluation', 'Inscrire'];

    function EleveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Eleve, Evaluation, Inscrire) {
        var vm = this;

        vm.eleve = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.evaluations = Evaluation.query();
        vm.inscrires = Inscrire.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.eleve.id !== null) {
                Eleve.update(vm.eleve, onSaveSuccess, onSaveError);
            } else {
                Eleve.save(vm.eleve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:eleveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datenaissance = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
