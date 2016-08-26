(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EcoleDialogController', EcoleDialogController);

    EcoleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ecole'];

    function EcoleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Ecole) {
        var vm = this;

        vm.ecole = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ecole.id !== null) {
                Ecole.update(vm.ecole, onSaveSuccess, onSaveError);
            } else {
                Ecole.save(vm.ecole, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:ecoleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
