(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('InscrireDialogController', InscrireDialogController);

    InscrireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inscrire', 'Eleve', 'Classe', 'AnneeAcademique'];

    function InscrireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Inscrire, Eleve, Classe, AnneeAcademique) {
        var vm = this;

        vm.inscrire = entity;
        vm.clear = clear;
        vm.save = save;
        vm.eleves = Eleve.query();
        vm.classes = Classe.query();
        vm.anneeacademiques = AnneeAcademique.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.inscrire.id !== null) {
                Inscrire.update(vm.inscrire, onSaveSuccess, onSaveError);
            } else {
                Inscrire.save(vm.inscrire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:inscrireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
