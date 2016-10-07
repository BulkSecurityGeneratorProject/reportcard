(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EstDispenseDialogController', EstDispenseDialogController);

    EstDispenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EstDispense', 'AnneeAcademique', 'Matiere', 'User'];

    function EstDispenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EstDispense, AnneeAcademique, Matiere, User) {
        var vm = this;

        vm.estDispense = entity;
        vm.clear = clear;
        vm.save = save;
        vm.anneeacademiques = AnneeAcademique.query();
        vm.matieres = Matiere.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.estDispense.id !== null) {
                EstDispense.update(vm.estDispense, onSaveSuccess, onSaveError);
            } else {
                EstDispense.save(vm.estDispense, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:estDispenseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
