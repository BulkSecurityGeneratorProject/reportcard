(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('ClasseDialogController', ClasseDialogController);

    ClasseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Classe', 'Inscrire'];

    function ClasseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Classe, Inscrire) {
        var vm = this;

        vm.classe = entity;
        vm.clear = clear;
        vm.save = save;
        vm.inscrires = Inscrire.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.classe.id !== null) {
                Classe.update(vm.classe, onSaveSuccess, onSaveError);
            } else {
                Classe.save(vm.classe, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:classeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
